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

  if (empty($id)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else {
    mysqli_select_db($connect, "Member");
    $query_mysql="SELECT * FROM Member WHERE ID = BINARY('$id')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      if (mysqli_num_rows($result) == 1) {
        $result_each = mysqli_fetch_assoc($result);
        echo $result_each['Name'];
        echo ",";
        echo $result_each['ID'];
        echo ",";
        echo $result_each['Introduce'];
        echo ",";
      }
    } else {
      $json_data = array (
        "result" => "Unauthorized(3)"
      );
      echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
    }
  }

  mysqli_close($connect);
?>
