# -*- coding: utf-8 -*-
'''

@author: epsilonyuan@gmail.com
'''

from radial_core_test_util import *

if __name__ == "__main__":
    sigma = 3.0
    r = symbols("r")
    func = 1 / (sigma * sqrt(2 * pi)) * exp(-r ** 2 / sigma ** 2 / 2)
    distance_samples = list(np.linspace(0, sigma * 2.1, 5))
    distance_square_samples = list(np.linspace(0, sigma * 2.2, 5))
    
    print_json_test_data(func, r, distance_samples, distance_square_samples,False)
