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


def encode_features(row, has_output=True):
    row = str_to_float(row, has_output)
    return row


def encode_train_data(array):
    categories = []
    for row in array:
        row = encode_features(row)
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


def str_to_float(array, has_output=True):
    to_range = len(array) - 1 if has_output else len(array)

    for i in range(0, to_range):
        array[i] = float(array[i])
    return array


def get_num_of_features(array):
    return len(array[0][:-1])


def get_init_weights(array):
    weight_list = []
    for i in range(0, get_num_of_features(array)):
        weight_list.append(rnd.random())
    # Bias
    weight_list.append(rnd.random())
    print(weight_list)
    return weight_list


def calc_output(theta, features, weights, array):
    dot_prod_calc = 0.0
    for i in range(0, get_num_of_features(array)):
        dot_prod_calc += features[i] * weights[i]
    dot_prod_calc += 1 * weights[-1]
    return 1 if dot_prod_calc >= theta else 0


def train_model(train_array, max_iter=100, learning_rate=0.1, max_error=0, theta=0):
    weights = get_init_weights(train_array)
    num_of_features = get_num_of_features(train_array)

    iteration = 0
    while iteration <= max_iter:
        error = 0
        for x in train_array:
            output = calc_output(theta, x, weights, train_array)
            modifier = x[-1] - output
            for i in range(num_of_features):
                weights[i] += learning_rate * modifier * x[i]
            error += math.fabs(modifier)
        iteration += 1
        print(f"ITER: {iteration} ERROR: {error / len(train_array)}")
        if error <= max_error:
            break
    return weights


def test_model(weights, test_set, theta):
    acc = 0

    for x in test_set:
        output = calc_output(theta, x, weights, test_set)

        if output == x[-1]:
            acc += 1
            print(f"BANG {x} = {output}")
        else:
            print(f"    DAMN {x} = {output}")
    return int((acc / len(test_set)) * 100)


if __name__ == '__main__':
    threshold = 0

    train_data = get_data('perceptron.data')
    train_data, c1, c2 = encode_train_data(train_data)
    model = train_model(train_data, theta=threshold)

    print(model)

    test_data = get_data('perceptron.test.data')
    test_data = encode_test_data(test_data, c1, c2)
    accuracy = test_model(model, test_data, threshold)

    print(f'\nAccuracy is {accuracy}%')
    print('Type in your vector for classification in form "x1,x2,etc"')
    print(f'No more or less than {get_num_of_features(train_data)} features')
    vector = input()
    vector = vector.split(',')
    vector = encode_features(vector, has_output=False)

    v_calc = calc_output(threshold, vector, model, train_data)
    v_cat = c1.name if v_calc == c1.num else c2.name
    print(f'Your vector was classified as {v_cat}')
