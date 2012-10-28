from com.orbit.core import EntityScript
import Level3

class Entity(EntityScript):
    
    def __init__(self):
        self.data = {}

    def onInteract(self):

        self.data['appendMessage'] = 'Lamp 1!'
        self.data['destroy'] = 'TRUE'

        return self.data
