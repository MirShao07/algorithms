from collections import deque

# Use BFS to find shortest augmenting paths
# Edmonds-Karp
def BFS(graph,start,end):
    paths = {start:[]}
    queue = deque()
    queue.append(start)
    if start == end:
        return paths[start]
    while queue:
        node = queue.popleft()
        for key, val in graph[node].items():
            residual = val[0] - val[1]
            if residual > 0 and key not in paths:
                paths[key] = paths[node] + [(node,key,residual)]
                if key == end:
                    return paths[key]
                queue.append(key)


def calculate_maxflow(graph):
    path = BFS(graph,'SOURCE','SINK')
    while path:
        bottle_neck = min([edge[2] for edge in path])
        for edge in path:
            graph[edge[0]][edge[1]][1] += bottle_neck
            if edge[0] not in graph[edge[1]]:
                graph[edge[1]][edge[0]] = [0,-bottle_neck]
            else:
                graph[edge[1]][edge[0]][1] -= bottle_neck
        path = BFS(graph,'SOURCE','SINK')

    return sum([val[1] for key,val in graph['SOURCE'].items()])


# Below is the formulation of the original problem.

"""
Given a set of power outlets of different types
A set of laptops(plugs of different types)
A set of adapters(unlimied supply of each type)
Match them up to minimise the number of laptops without power.
Input in the format of [3,"NZ","British","British",3,"NZ","British","NZ",2,"NZ American","American British"]
"""
def graph_helper(outlets,laptops,adapters):
    #initialise network, represented by a dictionary of dictionaries
    fn = {}
    #add all nodes and outer edges (source and sink)
    fn['SOURCE'] = {}
    fn['SINK'] = {}
    for key, val in outlets.items():
        real_key = key+'_F'
        fn[real_key] = {}
        fn['SOURCE'][real_key] = [val,0]
    for key, val in laptops.items():
        real_key = key+'_M'
        fn[real_key] = {}
        fn[real_key]['SINK'] = [val,0]
    for pair in adapters:
        female = pair[0]+'_F'
        male = pair[1]+'_M'
        if female not in fn:
            fn[female] = {}
        if male not in fn:
            fn[male] = {}
    #add inner edges
    for key in outlets:
        if key+'_M' in fn:
            fn[key+'_F'][key+'_M'] = [float('inf'),0]
        if key in laptops:
            flow = min(outlets[key],laptops[key])
            fn[key+'_F'][key+'_M'][1] = flow
            fn[key+'_M'][key+'_F'] = [0,-flow]
            fn['SOURCE'][key+'_F'][1] = flow
            fn[key+'_F']['SOURCE'] = [0,-flow]
            fn[key+'_M']['SINK'][1] = flow
            fn['SINK'][key+'_M'] = [0,-flow]

    for pair in adapters:
        fn[pair[1]+'_M'][pair[0]+'_F'] = [float('inf'),0]
        if pair[0]+'_M' in fn:
            fn[pair[0]+'_F'][pair[0]+'_M'] = [float('inf'),0]
    #for key,val in fn.items():
        #print(key,val)
    return fn

def turn_to_graph(input,out,lap):
    outlets = {}
    laptops = {}
    adapters = set()
    for i in range(1,out+1):
        outlets[input[i]] = outlets.get(input[i],0) + 1
    for j in range(out + 2, out + lap + 2):
        laptops[input[j]] = laptops.get(input[j],0) + 1
    for k in range(out + lap + 3,len(input)):
        pair = input[k].split()
        adapters.add((pair[0],pair[1]))
    return graph_helper(outlets,laptops,adapters)


def main():
    lst = [4,"A","B","C","D",5,"B","C","B","B","X",3,"B X","A X","X D"]
    out_num = lst[0]
    laptop_num = lst[out_num+1]
    adapter_num = lst[out_num+laptop_num+2]
    graph = turn_to_graph(lst,out_num,laptop_num)
    max_flow = calculate_maxflow(graph)
    print(laptop_num-max_flow)

if __name__ == '__main__':
    main()
