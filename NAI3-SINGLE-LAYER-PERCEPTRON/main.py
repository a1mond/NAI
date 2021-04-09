import collections
import os
import sys
from collections import Counter
from string import ascii_lowercase

import numpy as np

CLASS_NUM = int(sys.argv[1])
INPUTS_NUM = 26
CATEGORIES = {}


def read_files(path):
    letter_list = []
    for subdir, dirs, files in os.walk(path):
        for file in files:
            letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                       't',
                       'u', 'v', 'w', 'x', 'y', 'z']
            with open(os.path.join(path, file), mode='r', encoding='utf-8') as f:
                for line in f:
                    for letter in line:
                        if letter.lower() in ascii_lowercase:
                            letters.append(letter.lower())
            letters = collections.OrderedDict(sorted(Counter(letters).items()))
            count = sum(letters.values())
            letter_list.append([num / count for num in letters.values()])

    return letter_list


def get_data(path):
    dataset = []
    for subdir, dirs, files in os.walk(path):
        for directory in dirs:
            data = read_files(os.path.join(path, directory))
            data_arr = np.array(data)
            if directory not in CATEGORIES:
                label = np.zeros(CLASS_NUM)
                label[-CATEGORIES.__len__()] = 1
                CATEGORIES[directory] = label
                labels = np.repeat([np.array(label)], repeats=data_arr.shape[0], axis=0)
                data_arr = np.hstack([data_arr, labels])
            else:
                label = CATEGORIES[directory]
                labels = np.repeat([np.array(label)], repeats=data_arr.shape[0], axis=0)
                data_arr = np.hstack([data_arr, labels])
            dataset.append(data_arr)
    return dataset


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


def sigma(d, y):
    return (d - y) * y * (1 - y)


def predict(x, w):
    return sigmoid(x.dot(w))


def init_weights():
    return np.random.rand(INPUTS_NUM, CLASS_NUM)


def train(x_matrix, theta=np.ones((CLASS_NUM, 1)), learning_rate=0.2, epochs=1000):
    w = init_weights()
    for i in range(epochs):
        for item in x_matrix:
            for x in item:
                _x = np.array(x[:-CLASS_NUM])
                d = np.array(x[-CLASS_NUM:])

                net = np.array(np.matrix(np.dot(w.T, _x)).T - theta).flatten()

                y = sigmoid(net)
                sig = np.matrix(sigma(d, y)).T

                w = w.T + learning_rate * np.outer(sig, _x)
                w = w.T

                theta = theta - learning_rate * sig
    return w, theta


def get_key(value):
    for key, val in CATEGORIES.items():  # for name, age in dictionary.iteritems():  (for Python 2.x)
        if (val == value).all():
            return key

    return -1


def classify(arr):
    return get_key(normalize_sigmoid(arr))


def normalize_sigmoid(arr):
    new_arr = np.zeros(3)
    index = np.where(arr == np.amax(arr))[0][0]
    new_arr[index] = 1
    return new_arr


def test(x_matrix, w, theta):
    for item in x_matrix:
        for x in item:
            _x = np.array(x[:-CLASS_NUM])
            net = np.array(np.matrix(np.dot(w.T, _x)).T - theta).flatten()
            y = sigmoid(net)
            classified = classify(y)
            print(f'{x[-CLASS_NUM:]} --- {normalize_sigmoid(y)} --- {classified}')


if __name__ == "__main__":
    w_trained, theta_trained = train(get_data('dataset/train'))
    test(get_data('dataset/test'), w_trained, theta_trained)
