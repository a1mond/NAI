import numpy as np


def normalize_sigmoid_output(arr):
    new_arr = np.zeros(3)
    index = np.where(arr == np.amax(arr))[0][0]
    new_arr[index] = 1
    return new_arr


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


def sigma(d, y):
    return (d - y) * y * (1 - y)


class SingleLayerPerceptron:
    def __init__(self, class_num, inputs_num, categories):
        self.class_num = class_num
        self.inputs_num = inputs_num
        self.categories = categories

        self.w = None
        self.theta = None

    def get_key(self, value):
        for key, val in self.categories.items():  # for name, age in dictionary.iteritems():  (for Python 2.x)
            if (val == value).all():
                return key
        return -1

    def classify(self, arr):
        return self.get_key(normalize_sigmoid_output(arr))

    def init_weights(self):
        return np.random.rand(self.inputs_num, self.class_num)

    def fit(self, x_matrix, learning_rate=0.2, epochs=1000):
        self.w = self.init_weights()
        self.theta = np.ones((self.class_num, 1))
        for i in range(epochs):
            for item in x_matrix:
                for x in item:
                    _x = np.array(x[:-self.class_num])
                    d = np.array(x[-self.class_num:])

                    net = np.array(np.matrix(np.dot(self.w.T, _x)).T - self.theta).flatten()

                    y = sigmoid(net)
                    sig = np.matrix(sigma(d, y)).T

                    self.w = self.w.T + learning_rate * np.outer(sig, _x)
                    self.w = self.w.T

                    self.theta = self.theta - learning_rate * sig

    def predict(self, x_matrix):
        correct = 0
        i = 0
        for item in x_matrix:
            for x in item:
                i += 1
                _x = np.array(x[:-self.class_num])
                net = np.array(np.matrix(np.dot(self.w.T, _x)).T - self.theta).flatten()
                y = sigmoid(net)
                classified = self.classify(y)
                if np.array_equal(normalize_sigmoid_output(y), x[-self.class_num:]):
                    correct += 1
                print(f'{x[-self.class_num:]} --- {normalize_sigmoid_output(y)} --- {classified}')
        print(f'\nAccuracy of predict method is: {round((correct / i), 2) * 100}%')
