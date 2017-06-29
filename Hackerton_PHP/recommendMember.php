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

  $key = mysqli_real_escape_string($connect, $_POST['key']);

  if (empty($key)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else {
    mysqli_select_db($connect, "Project");
    $query_mysql="SELECT * FROM Project WHERE ProKey = BINARY('$key')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      if (mysqli_num_rows($result) == 1) {
        $result_each = mysqli_fetch_assoc($result);
        $android = $result_each['AndroidFlag'];
        $web = $result_each['WebFlag'];
        $ios = $result_each['IOSFlag'];
        $design = $result_each['DesignFlag'];

        $flag = 0;
        $query_mysql = "SELECT * FROM Member WHERE ";

        if ($android == 1) {
          $query_mysql .= "AndroidFlag = 1";
          $flag = 1;
        }
        if ($web == 1) {
          if ($flag == 0) {
            $query_mysql .= "WebFlag = 1";
            $flag = 1;
          } else {
            $query_mysql .= " or WebFlag = 1";
          }
        }
        if ($ios == 1) {
          if ($flag == 0) {
            $query_mysql .= "IOSFlag = 1";
            $flag = 1;
          } else {
            $query_mysql .= " or IOSFlag = 1";
          }        }
        if ($design == 1) {
          if ($flag == 0) {
            $query_mysql .= "DesignFlag = 1";
            $flag = 1;
          } else {
            $query_mysql .= " or DesignFlag = 1";
          }
        }
        mysqli_select_db($connect, "Member");
        if ($result = mysqli_query($connect, $query_mysql)) {
          $number = mysqli_num_rows($result);
          echo $number;
          echo ",";
          if ($number > 0) {
            while($row = mysqli_fetch_assoc($result)){
               echo $row['ID'];
               echo ",";
               echo $row['Name'];
               echo ",";
            }
          } else {
            echo "empty";
          }
        } else {
          $json_data = array (
            "result" => "Unauthorized(3)"
          );

          echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
        }
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
