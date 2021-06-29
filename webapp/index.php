
<?php 

//db connection detail
$db_host = "localhost";
$db_port = "3306";
$db_name = "demo1";
$db_user = "demo";
$db_pass = "Demo12$1";


//our products
$productlist = array (
    array("Headphones",1500),
    array("Earphones",350),
    array("Mouse",1000)
  );


//initialize order details
$product = null; 
$quantity = null; 
$name = null; 
$phonenumber = null; 
$price = 0; 
$total = 0; 
$paybill = "4041855";

if(isset($_POST['product'])){
    $product = $_POST['product'];

    //get the price 
    foreach($productlist as $prod){
        if($prod[0] == $product){
            $price = $prod[1]; //price is the second value in product array 
        }
    }

}

if(isset($_POST['quantity'])){
    $quantity = $_POST['quantity'];
}

if(isset($_POST['name'])){
    $name = $_POST['name'];
}

if(isset($_POST['phonenumber'])){
    $phonenumber = $_POST['phonenumber'];
}

$total = $price * $quantity;


//check if form has been submitted (posted)

if(!empty($_POST)){

    if(!isset($product) || 
    !isset($quantity) || 
    !isset($name) || 
    !isset($phonenumber)) {

        echo "WARNING: Some fields are missing. Product: $product, quantity: $quantity, name: $name, phonenumber: $phonenumber"; 

    } else {

        //connect to the database and insert the record
        $conn = mysqli_connect($db_host, $db_user, $db_pass);

        if($conn){

            if (mysqli_select_db($conn, $db_name)) {

                $query = "INSERT INTO orders(product,quantity,name, phonenumber, price,total,status,created_on, last_updated_on) VALUES (?,?,?, ?, ?,?,'NEW',NOW(), NOW());";

                if ($stmt = mysqli_prepare($conn, $query)) {

                    mysqli_stmt_bind_param($stmt, 'ssssss', $product, $quantity, $name, $phonenumber, $price, $total);

                    if(mysqli_stmt_execute($stmt)){

                        $last_insert_id = mysqli_insert_id($conn);

                        echo "Order submitted successfully. Order id: $last_insert_id. You will receive an SMS shortly on $phonenumber"; 

                    } else {
                        echo "Unable to insert the database. Query: $query"; 
                    }


                } else {
                    echo "Unable to prepare the statement. Query: $query"; 
                }

            } else {
                echo "Unable to select to the database."; 
            }

        } else {
            echo "Unable to connect to the database."; 
        }

    }

}


?>


<!DOCTYPE html>
<html>
<head>
    <title>Threads App</title>
</head>
<body>

<h2>Demo Web App</h2>

<form method="post">
    <label for="product">Select Product:</label><br>

    <select name="product" id="product">
    <?php 

        foreach ($productlist as $product) {

            echo '<option value="'.$product[0].'">'.$product[0].' - KES '.$product[1].'</option>';

        }
    ?>
    </select><br>
    <label for="quantity">Quantity:</label><br>
    <input type="text" id="quantity" name="quantity" value="1"><br>
    <label for="name">Name:</label><br>
    <input type="text" id="name" name="name" value=""><br>
    <label for="phonenumber">Phone Number:</label><br>
    <input type="text" id="phonenumber" name="phonenumber" value=""><br><br>
    <input type="submit" value="Submit">
</form>

</body>
</html>


