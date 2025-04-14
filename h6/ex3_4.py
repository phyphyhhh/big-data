from pyspark.sql import SparkSession
import numpy as np

spark = SparkSession.builder.appName("BatchGradientDescentBroadcast").getOrCreate()

data_path = "datasets/PBMC_16k_RNA.csv"
data = spark.read.csv(data_path, header=True, inferSchema=True)

features = data.columns
features.remove("KLHL17")
data = data.select(*features, "KLHL17")

data_rdd = data.rdd.map(lambda row: (row["KLHL17"], np.array([row[feature] for feature in features])))

learning_rate = 0.01
n_iterations = 1000
n_features = len(features)

weights = np.zeros(n_features + 1)

for iteration in range(n_iterations):
    weights_broadcast = spark.sparkContext.broadcast(weights)
    predictions = data_rdd.map(lambda row: (row[0], np.dot(weights_broadcast.value[1:], row[1]) + weights_broadcast.value[0]))
    gradients = predictions.map(lambda p: (p[1] - p[0],) + tuple((p[1] - p[0]) * x for x in row[1])).reduce(lambda a, b: tuple(a[i] + b[i] for i in range(len(a))))
    gradients = np.array(gradients)
    weights[0] -= learning_rate * gradients[0] / data.count()
    weights[1:] -= learning_rate * gradients[1:] / data.count()

print("Final weights:", weights)

spark.stop()
