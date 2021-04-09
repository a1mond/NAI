class Category:
    def __init__(self, name, num):
        self.name = name
        self.num = num

    def __eq__(self, o: object) -> bool:
        return super.__name__.__eq__(o.__name__)

    def __hash__(self) -> int:
        return super.__name__.__hash__()
