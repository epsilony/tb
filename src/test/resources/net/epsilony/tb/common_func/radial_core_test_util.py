# -*- coding: utf-8 -*-
'''

@author: epsilonyuan@gmail.com
'''

from sympy import *
import numpy as np
import json
from io import StringIO

def print_json_test_data(func, r, distance_samples, distance_square_samples, zero_over_unit=True):
    func_diff = func.diff(r)
        
    results_by_distance = []
    for s in distance_samples:
        if s>=1 and zero_over_unit:
            results_by_distance.append([0.0,0.0])
            continue
        results_by_distance.append((float(func.subs(r, s).evalf()), float(func_diff.subs(r, s).evalf())))
    
    u = symbols("u")
    
    func_square = func.subs(r, sqrt(u))
    func_square_diff = func_square.diff(u)
    
    results_by_distance_square = []
    func_squares = (func_square, func_square_diff)
    for s in distance_square_samples:
        if s>=1 and zero_over_unit:
            results_by_distance_square.append([0.0,0.0])
            continue
        values = []
        for f in func_squares:
            values.append(float(f.subs(u, s).evalf()))
        results_by_distance_square.append(values)
        
    
    data = {
          'distanceSamples':distance_samples,
          'resultsByDistance':results_by_distance,
          'distanceSquareSamples':distance_square_samples,
          'resultsByDistanceSquare':results_by_distance_square}
    str_io = StringIO()
    json.dump(data, str_io)
    print(str_io.getvalue())