from com.orbit.core import EntityScript

class Entity(EntityScript):
    
    def __init__(self):
        self.data = {}

    def onInteract(self):
        self.data['level'] = 'Level2.xml'
        self.data['state'] = 'none'
        
        return self.data
