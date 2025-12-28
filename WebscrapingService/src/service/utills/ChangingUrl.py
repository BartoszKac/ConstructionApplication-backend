class ChangeUrl:

    addPath = "&page="


    @classmethod
    def changeUrl(cls, url: str,page):
        return url + cls.addPath + str(page)
        pass
