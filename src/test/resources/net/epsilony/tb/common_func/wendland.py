# -*- coding: utf-8 -*-
'''

@author: epsilonyuan@gmail.com
'''

from radial_core_test_util import *
import sys

if __name__ == "__main__":
    r = symbols("r")
    wendlands = {
    "wendland_3_1" : (1 - r) ** 4 * (4 * r + 1),
    "wendland_3_2" : (1 - r) ** 6 * (35 * r ** 2 + 18 * r + 3),
    "wendland_3_3" : (1 - r) ** 8 * (32 * r ** 3 + 25 * r ** 2 + 8 * r + 1)
    }
    
    if len(sys.argv)>1:
        func_key = sys.argv[1]
    else:
        func_key = "wendland_3_1"
    func = wendlands[func_key]
    
    distance_samples = list(np.linspace(0, 1.2, 7))
    distance_square_samples = list(np.linspace(0, 1.3, 7))
    
    print_json_test_data(func, r, distance_samples, distance_square_samples)
    
