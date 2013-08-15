# -*- coding: utf-8 -*-
'''

@author: epsilonyuan@gmail.com
'''

from radial_core_test_util import *

import sys
def gen_test_set(test1):
    r=symbols('r')
    if test1:
        distance_samples=list(np.linspace(0,0.5,5))
        distance_square_samples=list(np.linspace(0,0.25,5))
        func=4*r**3-4*r**2+2/3.0
    else:
        distance_samples=list(np.linspace(0.5,1.1,5))
        distance_square_samples=list(np.linspace(0.25,1.2,5))
        func=-4/3.0*r**3+4*r**2-4*r+4/3.0
    return (func,r,distance_samples,distance_square_samples)
if __name__=="__main__":
    
    if len(sys.argv)<2:
        test1=False
    else:
        test1=sys.argv[1].lower()=='test1'
    test1=True
    print_json_test_data(*(gen_test_set(test1)))