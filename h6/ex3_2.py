from pyspark.sql import SparkSession
from pyspark.sql.functions import col
import numpy as np

spark = SparkSession.builder.appName("GradientDescent").getOrCreate()

data_path = "datasets/PBMC_16k_RNA.csv"
data = spark.read.csv(data_path, header=True, inferSchema=True)

data.printSchema()

features = data.columns
features.remove("KLHL17")
data = data.select(*features, "KLHL17")

data_rdd = data.rdd.map(lambda row: (row["KLHL17"], np.array([row[feature] for feature in features])))

learning_rate = 0.01
n_iterations = 1000
n_features = len(features)

weights = np.zeros(n_features + 1) 

for iteration in range(n_iterations):
    for row in data_rdd.collect():
        prediction = np.dot(weights[1:], row[1]) + weights[0]

        error = prediction - row[0]
        gradients = np.array([error] + [error * x for x in row[1]])

        weights[0] -= learning_rate * gradients[0] 
        weights[1:] -= learning_rate * gradients[1:]

print("Final weights:", weights)

spark.stop()