class TestType {
    id: number;
    name: string;
    extraInfo: string;

    constructor(id: number, name: string, extraInfo: string) {
        this.id = id;
        this.name = name;
        this.extraInfo = extraInfo;
    }

    toString() {
        return "id: " + this.id + ", name: " + this.name + ", extraInfo: " + this.extraInfo;
    }
}

let testType = new TestType(1, "Bent Tonsse", "Mandrilseer nummer 1 milliard");
console.log(testType.toString());