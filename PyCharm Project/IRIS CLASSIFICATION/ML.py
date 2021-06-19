import pandas as pd  # data processing, CSV file I/O (e.g. pd.read_csv)
import pandas_profiling as pf
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import LabelEncoder

# read dataset
iris = pd.read_csv("DataSet/IRIS.csv")

# show the first 5 records
iris.info()

# make the id as type string
iris['Id'] = iris['Id'].astype(str)
iris.describe()

# make a visualization report
profile = pf.ProfileReport(iris, title='IRIS PREDICTION REPORT')
profile.to_file(output_file='MyReport/Iris_Report.html')

# Data cleaning
print(iris.isnull().sum())
a = LabelEncoder()
a1 = a.fit(iris['Species'])
iris_label = a1.transform(iris['Species'])
iris['iris_label'] = iris_label
print(pd.Series(iris_label).unique())
print(iris)

# label_mapping = dict(zip(pd.Series(iris_label).unique(), iris['Species']))
# print(label_mapping)

# Train Test Split
X = iris[['SepalLengthCm', 'SepalWidthCm', 'PetalLengthCm', 'PetalWidthCm']]
y = iris['iris_label']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33, random_state=42)

# training Classifier model


kNN = KNeighborsClassifier(n_neighbors=15)
kNN.fit(X_train, y_train)

print(kNN.score(X_test, y_test))

# Predict unknown data using classifier

predict_iris = kNN.predict([[3, 4, 3, 0.2]])
output = predict_iris[0]
if output == 0:
    print("Iris Setosa")
elif output == 1:
    print("Iris Versicolor")
elif output == 2:
    print("Iris Virginica")

# mobile app
model = tf.keras.Sequential(
    [tf.keras.layers.Dense(units=4, input_shape=[4]), tf.keras.layers.Dense(units=1, input_shape=[1])])
model.compile(optimizer='sgd', loss='mean_squared_error')
model.fit(X, y, epochs=500)
keras_file = 'linear.h5'
tf.keras.models.save_model(model, keras_file)
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tfmodel = converter.convert()
open("linear.tflite", "wb").write(tfmodel)
