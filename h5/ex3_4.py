import pandas as pd
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
import numpy as np

sensors1_df = pd.read_csv('D:/VE472/HaoyunPan522370910136-hw/hw5/sensors1.csv')
sensors2_df = pd.read_csv('D:/VE472/HaoyunPan522370910136-hw/hw5/sensors2.csv')

scaler = StandardScaler()
sensors1_scaled = scaler.fit_transform(sensors1_df)
sensors2_scaled = scaler.transform(sensors2_df)

pca1 = PCA(n_components=0.90)
pca2 = PCA(n_components=0.90)

sensors1_pca = pca1.fit_transform(sensors1_scaled)
sensors2_pca = pca2.fit_transform(sensors2_scaled)

similarity = np.dot(pca1.components_, pca2.components_.T)

print("Similarity matrix between the PCA components of sensors1 and sensors2:")
print(similarity)

threshold = 0.9
if np.any(similarity >= threshold):
    print("There are components in sensors2.csv that are similar to those in sensors1.csv.")
else:
    print("There are no components in sensors2.csv that are similar to those in sensors1.csv.")
