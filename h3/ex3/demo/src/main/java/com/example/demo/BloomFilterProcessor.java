package com.example.demo;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.bloom.BloomFilter;
import org.apache.hadoop.util.bloom.Key;
import org.apache.hadoop.util.hash.Hash;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.BitSet;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class BloomFilterProcessor {

    public static class BloomFilterMapper extends Mapper<LongWritable, Text, Text, BloomFilter> {

        private BloomFilter bloomFilter = new BloomFilter(1000, 5, Hash.MURMUR_HASH);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",");
            if (fields.length == 3) {
                String studentId = fields[1].trim();
                if (studentId.endsWith("3")) {
                    bloomFilter.add(new Key(studentId.getBytes()));
                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            context.write(new Text("bloomFilter"), bloomFilter);
        }
    }

    public static class BloomFilterReducer extends Reducer<Text, BloomFilter, Text, Text> {

        private MultipleOutputs<Text, Text> multipleOutputs;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            multipleOutputs = new MultipleOutputs<>(context);
        }

        @Override
        protected void reduce(Text key, Iterable<BloomFilter> values, Context context) throws IOException, InterruptedException {
            BloomFilter combinedBloomFilter = new BloomFilter(1000, 5, Hash.MURMUR_HASH);

            for (BloomFilter bloomFilter : values) {
                combinedBloomFilter.or(bloomFilter);
            }

            String schemaJson = "{\n" +
                    "  \"type\": \"record\",\n" +
                    "  \"name\": \"BloomFilterRecord\",\n" +
                    "  \"fields\": [\n" +
                    "    {\"name\": \"bitSet\", \"type\": \"bytes\"},\n" +
                    "    {\"name\": \"nbHash\", \"type\": \"int\"},\n" +
                    "    {\"name\": \"vectorSize\", \"type\": \"int\"}\n" +
                    "  ]\n" +
                    "}";
            Schema schema = new Schema.Parser().parse(schemaJson);

            BitSet bitSet;
            try {
                Field field = BloomFilter.class.getDeclaredField("bits");
                field.setAccessible(true);
                bitSet = (BitSet) field.get(combinedBloomFilter);
            } catch (Exception e) {
                throw new IOException("Unable to access bit set of BloomFilter", e);
            }

            byte[] bitSetBytes = bitSet.toByteArray();

            File tempFile = File.createTempFile("bloomFilter", ".avro");
            tempFile.deleteOnExit();
            GenericDatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
            DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
            dataFileWriter.create(schema, tempFile);

            GenericRecord record = new GenericData.Record(schema);
            record.put("bitSet", ByteBuffer.wrap(bitSetBytes));
            record.put("nbHash", 5);
            record.put("vectorSize", combinedBloomFilter.getVectorSize());

            dataFileWriter.append(record);
            dataFileWriter.close();

            context.write(key, new Text(tempFile.getAbsolutePath()));
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            multipleOutputs.close();
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Bloom Filter Job");

        job.setJarByClass(BloomFilterProcessor.class);
        job.setMapperClass(BloomFilterMapper.class);
        job.setReducerClass(BloomFilterReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BloomFilter.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        MultipleOutputs.addNamedOutput(job, "bloomFilter", org.apache.hadoop.mapreduce.lib.output.TextOutputFormat.class, Text.class, Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}



