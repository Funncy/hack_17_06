<?php
  header("Content-Type: text/html; charset=UTF-8");

  $hostname = "localhost";
  $database = "mattmatt96";
  $username = "mattmatt96";
  $password = "tlawogns96";
  $connect = mysqli_connect($hostname, $username, $password, $database);
  if (!$connect) {
    die('데이터베이스 접근에 문제가 있습니다.' . mysqli_error($connect));
  }

  mysqli_set_charset($connect, "utf8");

  $id = mysqli_real_escape_string($connect, $_POST['id']);
  $introduce = mysqli_real_escape_string($connect, $_POST['introduce']);

  if (empty($id)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else {
    mysqli_select_db($connect, "Member");
    $query_mysql="UPDATE Member SET Introduce='" . $introduce . "' WHERE ID = BINARY('$id')";
     if ($result = mysqli_query($connect, $query_mysql)) {
       echo "Authorized";
     } else {
       echo "Unauthorized(2)";
     }
  }

  mysqli_close($connect);
?>
