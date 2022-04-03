<?php

# Initialise common code
$preIP = dirname( __FILE__ );
require_once( "$preIP/../libs/includes/WebStart.php" );

$y=$_POST["y"];
$m=$_POST["m"];
$d=$_POST["d"];
$h=$_POST["h"];
$i=$_POST["i"];
$gender=$_POST["gender"];
$timezone=$_POST["timezone"];

if($y !== '' && $m !== '' && $d !== '' && $h !== '' && $i !== '') {
	if($timezone == '') $timezone = 'CCT';
	
	require_once( "$IP/LocalSettings.php" );
echo '{' . "\n";
	$birth = Date::get($y, $m, $d, $h, $i);
	$sz = new 八字命评($birth, $gender != '1');
	$sz->综合命评();
	$sz->流年行运();
echo '}';
}
