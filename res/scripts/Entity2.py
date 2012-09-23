from com.orbit.core import EntityScript

class Entity(EntityScript):
    
    def __init__(self):
        self.data = {}

    def onInteract(self):
        self.data['level'] = 'LevelSUPERAWESOME.xml'
        self.data['state'] = 'noneSUPERAWESOME'
        
        return self.data
