from com.orbit.core import EntityScript
import Level3

class Entity(EntityScript):
    
    def __init__(self):
        self.data = {}

    def onInteract(self):
        self.data['appendMessage'] = 'Lamp 3!'
        self.data['destroy'] = 'TRUE'
        self.data['destroyOther'] = ['res/entities/3/Lamp3_1.xml', 'res/entities/3/Lamp3_2.xml']

        return self.data
