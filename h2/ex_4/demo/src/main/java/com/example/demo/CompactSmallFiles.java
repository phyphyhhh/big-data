package com.example.demo;


import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class CompactSmallFiles {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: CompactSmallFiles <input_dir> <output_file> <schema_file>");
            System.exit(-1);
        }

        String inputDirPath = args[0];
        String outputFilePath = args[1];
        String schemaFilePath = args[2];

        try {
            Schema schema = new Schema.Parser().parse(new File(schemaFilePath));

            DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
            DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);

            CodecFactory snappyCodec = CodecFactory.snappyCodec();
            if (snappyCodec == null) {
                System.err.println("Failed to initialize Snappy codec.");
                System.exit(-1);
            }
            dataFileWriter.setCodec(snappyCodec);

            dataFileWriter.create(schema, new File(outputFilePath));

            File inputDir = new File(inputDirPath);
            if (!inputDir.isDirectory()) {
                System.err.println("Input path is not a directory: " + inputDirPath);
                System.exit(-1);
            }

            for (File file : inputDir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".bin")) {
                    String filename = file.getName();
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    String checksum = DigestUtils.sha1Hex(new FileInputStream(file));

                    GenericRecord record = new GenericData.Record(schema);
                    record.put("filename", filename);
                    record.put("filecontent", ByteBuffer.wrap(fileContent));
                    record.put("checksum", checksum);

                    dataFileWriter.append(record);
                }
            }

            dataFileWriter.close();
            System.out.println("Files have been successfully compacted into " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
