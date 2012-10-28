data = {}
max = 3
cur = 0

def inc():
    print("inc!")
    global cur
    cur += 1
    
    if (cur >= max):
        return True
    else:
        return False
