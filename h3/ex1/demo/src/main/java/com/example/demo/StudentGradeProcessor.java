package com.example.demo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class StudentGradeProcessor {

    public static class StudentGradeMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] tokens = StringUtils.split(line, ',');
            if (tokens.length == 3) {
                String studentID = tokens[1];
                String grade = tokens[2];
                context.write(new Text(studentID), new Text(grade));
            }
        }
    }

    public static class StudentGradeReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int maxGrade = Integer.MIN_VALUE;
            for (Text value : values) {
                int grade = Integer.parseInt(value.toString());
                maxGrade = Math.max(maxGrade, grade);
            }
            context.write(key, new Text(String.valueOf(maxGrade)));
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: StudentGradeProcessor <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Student Grade");
        job.setJarByClass(StudentGradeProcessor.class);
        job.setMapperClass(StudentGradeMapper.class);
        job.setReducerClass(StudentGradeReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}


