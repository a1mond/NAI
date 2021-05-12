import numpy as np
import pandas as pd


class NaiveBayesClassifier:
    def __init__(self):
        self._X = np.empty(shape=(1, 1))
        self._y = np.empty(shape=(1, 1))
        self.accuracy = 0
        self.classes = {}
        self.probabilities = {}

    def fit(self, X, y):
        self._X = X
        self._y = y
        for i in self._y:
            if i not in self.classes:
                self.classes[i] = 1
            else:
                self.classes[i] += 1
        for i in self.classes.items():
            self.classes[i[0]] = i[1] / X.shape[0]
        for i in range(0, X.shape[0]):
            for j in range(0, self._X.shape[1]):
                if (j, X[i, j], y[i]) not in self.probabilities.keys():
                    self.probabilities[(j, X[i, j], y[i])] = self.__find_att_prob((j, X[i, j], y[i]))

    def predict(self, X, y):
        good_predictions = 0
        for i in range(0, X.shape[0]):
            class_to_prob = {}
            for dep_class in self.classes.items():
                res = dep_class[1]
                for j in range(0, X.shape[1]):
                    if (j, X[i, j], dep_class[0]) in self.probabilities.keys():
                        res *= self.probabilities[(j, X[i, j], dep_class[0])]
                    else:
                        res *= 0
                class_to_prob[dep_class[0]] = res
            local_max = self.__find_max_in_set(class_to_prob)
            if local_max[0] == y[i]:
                good_predictions += 1
        self.accuracy = good_predictions / X.shape[0]

    @staticmethod
    def __find_max_in_set(class_to_prob):
        local_max = class_to_prob.popitem()
        for obj in class_to_prob.items():
            if obj[1] > local_max[1]:
                local_max = obj
        return local_max

    def __find_att_prob(self, att):
        occur = 0
        num = self._X.shape[0]
        for row, y in zip(self._X, self._y):
            if row[att[0]] == att[1] and att[2] == y:
                occur += 1
        occur = occur / num

        return occur


if __name__ == '__main__':
    dataset = pd.read_csv('agaricus-lepiota.data', header=None)
    train_set = pd.read_csv('agaricus-lepiota.test.data', header=None)
    X_train = dataset.iloc[:, 1:].values
    y_train = dataset.iloc[:, 0].values
    X_test = train_set.iloc[:, 1:].values
    y_test = train_set.iloc[:, 0].values
    classifier = NaiveBayesClassifier()
    classifier.fit(X_train, y_train)
    classifier.predict(X_test, y_test)
    print(classifier.accuracy)
