var TestType = /** @class */ (function () {
    function TestType(id, name, extraInfo) {
        this.id = id;
        this.name = name;
        this.extraInfo = extraInfo;
    }
    TestType.prototype.toString = function () {
        return "id: " + this.id + ", name: " + this.name + ", extraInfo: " + this.extraInfo;
    };
    return TestType;
}());
var testType = new TestType(1, "Bent Tonsse", "Mandrilseer nummer 1 milliard");
console.log(testType.toString());
