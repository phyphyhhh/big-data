package com.example.demo;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class AvroMaxGradeProcessor {

    public static class AvroFileMapper extends Mapper<AvroKey<GenericRecord>, NullWritable, Text, IntWritable> {
        @Override
        protected void map(AvroKey<GenericRecord> key, NullWritable value, Context context) throws IOException, InterruptedException {
            GenericRecord record = key.datum();
            ByteBuffer fileContentBuffer = (ByteBuffer) record.get("filecontent");
            String fileContent = StandardCharsets.UTF_8.decode(fileContentBuffer).toString().trim();
            String[] parts = fileContent.split(",");
            if (parts.length == 3) {
                String studentID = parts[1];
                int grade = Integer.parseInt(parts[2]);
                context.write(new Text(studentID), new IntWritable(grade));
            }
        }
    }

    public static class AvroFileReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int maxGrade = Integer.MIN_VALUE;
            for (IntWritable value : values) {
                maxGrade = Math.max(maxGrade, value.get());
            }
            context.write(key, new IntWritable(maxGrade));
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: AvroMaxGradeProcessor <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Avro Max Grade Processing");
        job.setJarByClass(AvroMaxGradeProcessor.class);
        job.setMapperClass(AvroFileMapper.class);
        job.setReducerClass(AvroFileReducer.class);
        job.setInputFormatClass(AvroKeyInputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}


