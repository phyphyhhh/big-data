package com.example.demo;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableFileInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class ExtractSmallFiles {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: ExtractSmallFiles <input_avro_file> <output_dir> <schema_file>");
            System.exit(-1);
        }

        String inputAvroFilePath = args[0];
        String outputDirPath = args[1];
        String schemaFilePath = args[2];

        try {
            Schema schema = new Schema.Parser().parse(new File(schemaFilePath));

            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
            DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(new SeekableFileInput(new File(inputAvroFilePath)), datumReader);

            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            GenericRecord record = null;
            while (dataFileReader.hasNext()) {
                record = dataFileReader.next(record);

                String filename = record.get("filename").toString();
                ByteBuffer filecontentBuffer = (ByteBuffer) record.get("filecontent");
                byte[] filecontent = new byte[filecontentBuffer.remaining()];
                filecontentBuffer.get(filecontent);
                String expectedChecksum = record.get("checksum").toString();

                File outputFile = new File(outputDir, filename);
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    fos.write(filecontent);
                }

                String actualChecksum = DigestUtils.sha1Hex(Files.newInputStream(outputFile.toPath()));
                if (!expectedChecksum.equals(actualChecksum)) {
                    System.err.println("Checksum mismatch for file: " + filename);
                } else {
                    System.out.println("Successfully extracted and verified file: " + filename);
                }
            }

            dataFileReader.close();
            System.out.println("All files have been successfully extracted to " + outputDirPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
