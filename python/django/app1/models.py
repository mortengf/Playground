from django.db import models

# Create your models here.

class Publication(models.Model):
    title = models.CharField(max_length=200)
    pub_date = models.DateTimeField('date published')
    
    def __unicode__(self):
        return self.title
