from com.orbit.core import EntityScript
import Level3

class Entity(EntityScript):
    
    def __init__(self):
        self.data = {}

    def onInteract(self):

        self.data['appendMessage'] = 'Lamp 4!'
        self.data['destroy'] = 'TRUE'
        ret = Level3.inc()
        if (ret):
            print("huzzah!!!")
        return self.data
