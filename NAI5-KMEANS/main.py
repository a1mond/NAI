import math
from argparse import ArgumentParser

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


class Point:
    def __init__(self, coords: list):
        self.coords = coords

    def __eq__(self, other):
        return self.coords == other.coords


class Centroid:
    def __init__(self, coords: list):
        self.coords = coords
        self.points = []

    def __eq__(self, other):
        return self.coords == other.coords


class KMeansClassifier:
    def __init__(self, num_classes: int):
        self.k = num_classes
        self.points = []
        self.centroids = []

    @staticmethod
    def __distance(point, centroid):
        calc = 0
        for x1, x2 in zip(point.coords, centroid.coords):
            calc += (x1 - x2) ** 2
        return math.sqrt(calc)

    def __init_lists(self, X):
        self.points = [Point(x) for x in X]
        self.centroids = [Centroid(x) for x in X[np.random.choice(X.shape[0], size=self.k, replace=False), :]]

    @staticmethod
    def __sum_of_cent_point_coords(centroids: list):
        calc = 0
        for centroid in centroids:
            for x in centroid.points:
                for z in x.coords:
                    calc += z
        return calc

    @staticmethod
    def __find_min(dist_list: list):
        minimum = None
        for item in dist_list:
            if minimum is None:
                minimum = item
                continue
            if item[1] < minimum[1]:
                minimum = item
        return minimum

    def __sum_of_distances(self):
        calc = 0
        for x in self.centroids:
            for z in x.points:
                calc += self.__distance(z, x)
        return calc

    def __extract_lengths(self):
        return [len(x.points) for x in self.centroids]

    def __recalculate_cent_coords(self):
        for centroid in self.centroids:
            coords_sum = [0 for _ in centroid.coords]
            for point in centroid.points:
                for i in range(0, len(centroid.coords)):
                    coords_sum[i] += point.coords[i]
            centroid.coords = [x / len(centroid.points) for x in coords_sum]

    def print_points_to_centroid(self):
        centroid = 1
        for x in self.centroids:
            print(f'\nCentroid {centroid}: \n')
            print(f'{np.matrix([x.coords for x in x.points])}')
            centroid += 1

    def fit(self, X):
        self.__init_lists(X)
        is_finished = False
        prev_lengths = []
        iteration = 1
        while not is_finished:
            for centroid in self.centroids:
                centroid.points = []
            for point in self.points:
                distances = []
                for centroid in self.centroids:
                    distances.append((centroid, self.__distance(point, centroid)))
                minimum = self.__find_min(distances)[0]
                for centroid in self.centroids:
                    if centroid is minimum:
                        centroid.points.append(point)
            if prev_lengths == self.__extract_lengths():
                is_finished = True
                continue
            self.__recalculate_cent_coords()
            prev_lengths = self.__extract_lengths()
            print(f'ITER: {iteration}, SOD: {self.__sum_of_distances()}')
            iteration += 1


if __name__ == '__main__':
    parser = ArgumentParser()
    parser.add_argument('-i', '--input_file', type=str, required=True)
    parser.add_argument('-n', '--num_classes', type=int, required=True)
    parsed = parser.parse_args()
    dataset = pd.read_csv(parsed.input_file)
    X_mat = dataset.iloc[:, :-1].values
    model = KMeansClassifier(parsed.num_classes)
    model.fit(X_mat)
    model.print_points_to_centroid()
    plt.scatter([x.coords[2] for x in model.centroids[0].points],
                [x.coords[3] for x in model.centroids[0].points], s=100, c='blue')
    plt.scatter([x.coords[2] for x in model.centroids[1].points],
                [x.coords[3] for x in model.centroids[1].points], s=100, c='green')
    plt.scatter([x.coords[2] for x in model.centroids[2].points],
                [x.coords[3] for x in model.centroids[2].points], s=100, c='brown')
    plt.scatter([x.coords[2] for x in model.centroids], [x.coords[3] for x in model.centroids], s=200, c='red')
    plt.show()
