def KMP_preprocess(pattern):
    m = len(pattern)
    reset_table = [0]*(m+1)
    reset_table[0] = -1
    i = 0
    j = -1
    while i<m:
        while j>-1 and pattern[i] != pattern[j]: j = reset_table[j]
        i += 1
        j += 1
        reset_table[i] = j
    return reset_table

def KMP(pattern,target,table):
    res = []
    m = len(pattern)
    n = len(target)
    i = 0
    j = 0
    while i<n:
        while j>-1 and target[i] != pattern[j]: j = table[j]
        i += 1
        j += 1
        if j==m:
            res.append(i-m)
            j = table[j]
    return res

def main():
    p = "ABABA"
    t = "ACABAABABDABABABABBABABA"
    table = KMP_preprocess(p)
    print("Reset table for {}:".format(p))
    print(table)
    res = KMP(p,t,table)
    print("{} is matched in {} at: ".format(p,t))
    print(res)

if __name__ == '__main__':
    main()
