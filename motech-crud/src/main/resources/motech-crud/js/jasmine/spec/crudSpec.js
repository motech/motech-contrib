describe("crudHelper", function() {

    it("should trim whitespaces from the given Json object", function() {
        var jsonObject =
            {property1 : "property1value", property2WithSpaces : "property2value   "};

        var trimmedData = trimmed(jsonObject);
        expect(trimmedData["property2WithSpaces"]).toEqual("property2value");
    });

    it("should trim whitespaces for properties with numeric values", function() {
        var jsonObject =
        {property1 : "property1value",
         property2WithNumericValue : "12345   "};

        var trimmedData = trimmed(jsonObject);
        expect(trimmedData["property2WithNumericValue"]).toEqual("12345");
    });

    it("should iterate through nested Json objects and trim whitespaces for properties", function() {
        var jsonObject =
        {property1 : "property1value",
            property2WithNumericValue : "12345   ",
            nestedJsonProperty:{
                field1: "field1Value    ",
                field2: "field2Value"
            }
        };

        var trimmedData = trimmed(jsonObject);
        expect(trimmedData["property2WithNumericValue"]).toEqual("12345");
        expect(trimmedData["nestedJsonProperty"]["field1"]).toEqual("field1Value");
    });

})