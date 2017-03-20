from django.contrib import admin
import models

class PublicationAdmin(admin.ModelAdmin):

    search_fields=('title',)
    list_filter=('pub_date',)

admin.site.register(models.Publication, PublicationAdmin)
