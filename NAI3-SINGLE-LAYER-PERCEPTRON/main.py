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


if __name__ == "__main__":
    from SingleLayerPerceptron import SingleLayerPerceptron
    classifier = SingleLayerPerceptron(CLASS_NUM, INPUTS_NUM, CATEGORIES)
    classifier.fit(get_data('dataset/train'))
    classifier.predict(get_data('dataset/test'))
