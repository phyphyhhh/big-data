from pyspark.sql import SparkSession
import numpy as np

spark = SparkSession.builder.appName("SteepestGradientDescent").getOrCreate()

data_path = "datasets/PBMC_16k_RNA.csv"
data = spark.read.csv(data_path, header=True, inferSchema=True)

features = data.columns
features.remove("KLHL17")
data = data.select(*features, "KLHL17")

data_rdd = data.rdd.map(lambda row: (row["KLHL17"], np.array([row[feature] for feature in features])))

n_iterations = 1000
n_features = len(features)

weights = np.zeros(n_features + 1)

for iteration in range(n_iterations):
    predictions = data_rdd.map(lambda row: (row[0], np.dot(weights[1:], row[1]) + weights[0]))
    gradients = predictions.map(lambda p: (p[1] - p[0],) + tuple((p[1] - p[0]) * x for x in row[1])).reduce(lambda a, b: tuple(a[i] + b[i] for i in range(len(a))))
    gradients = np.array(gradients)
    
    step_size = np.dot(gradients[1:], gradients[1:]) / np.dot(np.dot(data_rdd.map(lambda row: np.outer(row[1], row[1])).reduce(lambda a, b: a + b), gradients[1:]), gradients[1:])
    # Calculated the step size and updated the weights using the step size and gradients. 
    # The step size is computed using the square of the gradients and the Hessian matrix (approximated here using the outer product matrix of the features)

    weights[0] -= step_size * gradients[0] / data.count()
    weights[1:] -= step_size * gradients[1:] / data.count()

print("Final weights:", weights)

spark.stop()
