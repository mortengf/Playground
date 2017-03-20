print "hello world"

i = 8
s = "fest"

ia = [1,2,3,4,5] # list
# all lists are of type sequence
ta = (1,2,3)     # tuple, cannot change

week_days = dict()

print ia
print ia[0] == 1

class Person():
    default_country = 'Denmark'
    def __init__(self, name):
        self.country = self.default_country
        self.name = name

    def _set_country(self, country):
        self.country = country

    def to_str(self):
        print "Person"
        print "name : %s, from %s" % (self.name, self.country)

# nicer
# class Person(name)    

p1 = Person("Morten") #
p1.to_str()
# M from DK
p2 = Person("Karsten")
p2._set_country("Mars")
p2.to_str()
# K from M
