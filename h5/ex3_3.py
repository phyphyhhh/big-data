import pandas as pd
from sklearn.decomposition import PCA
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LinearRegression

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

X_n = X_pca[:, :n]
error = np.random.normal(0, 1, size=y.shape[0])

linear_model = LinearRegression()
linear_model.fit(X_n, y)

x0 = linear_model.intercept_
b = linear_model.coef_

print(f"Intercept: {x0}")
print(f"Coefficients: {b}")
