function login() {
    var username = $("#username").val();
    var password = $("#password").val();
    var token = $.ajax({
                    type: "GET",
                    url: '/user/token/'+ username +'/' + password,
                    async: false
                }).responseText;
    if(token.startsWith("Bearer")) {
        localStorage.setItem("username", username);
        localStorage.setItem("token", token);
        window.location.href = 'basket.html';
    }
    else
        alert('Bad credentials');
}

function basketOnLoad() {
    var productTypes = $.ajax({
                           type: "GET",
                           url: '/products/types',
                            headers: {
                                'Authorization': localStorage.getItem("token"),
                            },
                           async: false
                       }).responseText;
    productTypes = JSON.parse(productTypes);
    $.each(productTypes, function (i, item) {
        $('#productType').append($('<option>', {
            value: item,
            text : item
        }));
    });
    var username = localStorage.getItem("username");
    var products = $.ajax({
                          type: "GET",
                          url: '/user/'+username+'/basket',
                          headers: {
                            'Authorization': localStorage.getItem("token"),
                          },
                          async: false
                      }).responseText;
    products = JSON.parse(products);
    if(products.length === 0) {
        $('#basketEmptyText').show();
        $('#basketTable').hide();
    }
    else {
        $('#basketEmptyText').hide();
        $('#basketTable').show();
        $("#tbody").empty();
        $.each(products, function (i, item) {
            var markup = "<tr><td>"+item.name+"</td><td>"+item.type+"</td><td>"+item.description+"</td></tr>";
            $("#tbody").append(markup);
        });
    }
}

function addToBasket() {
    var productName = $("#productName").val();
    var quantity = $("#quantity").val();
    if(productName.match('[A-Za-z]+')) {
        if(quantity.match('[1-9]+')) {
            var productType = $('#productType :selected').text();
            var product = $.ajax({
                               type: "GET",
                               url: '/product/findByNameAndType?name='+productName+'&type='+productType,
                               headers: {
                                    'Authorization': localStorage.getItem("token"),
                               },
                               async: false
                           }).responseText;
            product = JSON.parse(product);
            var username = localStorage.getItem("username");
            $.ajax({
                   type: "POST",
                   url: '/user/basket/add/'+username+'/'+product.id+'/'+quantity,
                   headers: {
                        'Authorization': localStorage.getItem("token"),
                   },
                   async: false
               }).responseText;
            basketOnLoad();
        }
        else
            alert('Bad quantity format');
    }
    else
        alert('Bad product name format');
}
