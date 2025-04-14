import pandas as pd
from sklearn.decomposition import PCA
import numpy as np
from sklearn.preprocessing import StandardScaler

sensors1_df = pd.read_csv('D:/VE472/HaoyunPan522370910136-hw/hw5/sensors1.csv')

X = sensors1_df.iloc[:, :-1]
y = sensors1_df.iloc[:, -1]

scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

pca = PCA()
X_pca = pca.fit_transform(X_scaled)

explained_variance_ratio = pca.explained_variance_ratio_
cumulative_variance_ratio = np.cumsum(explained_variance_ratio)

n = np.where(cumulative_variance_ratio >= 0.90)[0][0] + 1

print(f'To explain 90% of the variance, we need {n} principal components.')
