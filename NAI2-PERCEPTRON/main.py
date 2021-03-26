import random as rnd
import math


class Category:
    def __init__(self, name, num):
        self.name = name
        self.num = num


def get_data(path):
    with open(path, mode='r') as csv_file:
        rows = []
        for row in csv_file:
            row = row.replace('\n', '')
            rows.append(row.split(','))
    return rows


def encode_train_data(array):
    categories = []
    for row in array:
        row = str_to_float(row)
        categories.append(row[-1])

    categories = set(categories)
    categories = list(categories)
    cat1 = Category(categories[0], 1)
    cat2 = Category(categories[1], 0)

    for row in array:
        if row[-1] == cat1.name:
            row[-1] = cat1.num
        else:
            row[-1] = cat2.num

    return array, cat1, cat2


def encode_test_data(array, cat1, cat2):
    for row in array:
        row = str_to_float(row)
        if row[-1] == cat1.name:
            row[-1] = cat1.num
        else:
            row[-1] = cat2.num
    return array


def str_to_float(array):
    for i in range(0, len(array) - 1):
        array[i] = float(array[i])
    return array


def get_num_of_features(array):
    return len(array[0][:-1])


def get_init_weights(array):
    weight_list = []
    for i in range(0, get_num_of_features(array) - 1):
        weight_list.append(rnd.random())
    # Bias
    weight_list.append(rnd.random())
    print(weight_list)
    return weight_list


def calc_output(theta, features, weights, array):
    dot_prod_calc = 0.0
    for i in range(0, get_num_of_features(array) - 1):
        dot_prod_calc += features[i] * weights[i]
    dot_prod_calc += 1 * weights[-1]
    return 1 if dot_prod_calc >= theta else 0


def train_model(train_array, max_iter=100, learning_rate=0.5, max_error=0, theta=0):
    weights = get_init_weights(train_array)
    num_of_features = get_num_of_features(train_array)

    iteration = 0
    while iteration <= max_iter:
        error = 0
        for x in train_array:
            output = calc_output(theta, x, weights, train_array)
            modifier = x[-1] - output
            for i in range(num_of_features - 1):
                weights[i] += learning_rate * modifier * x[i]
            error += math.fabs(modifier)
        iteration += 1
        print(f"ITER: {iteration} ERROR: {error/len(train_array)}")
        if error <= max_error:
            break
    return weights


def test_model(weights, train_set, theta):
    for x in train_set:
        output = calc_output(theta, x, weights, train_set)

        if output == x[-1]:
            print(f"BANG {x} = {output}")
        else:
            print(f"    DAMN {x} = {output}")


if __name__ == '__main__':
    threshold = 10

    train_data = get_data('perceptron.data')
    train_data, c1, c2 = encode_train_data(train_data)
    model = train_model(train_data, theta=threshold)

    print(model)

    test_data = get_data('perceptron.test.data')
    test_data = encode_test_data(test_data, c1, c2)
    test_model(model, train_data, threshold)
