<?php

function cutStr($sourcestr, $cutstart = 0, $cutlength = 0) {
	$returnstr = '';
	$i = 0;
	$n = 0;
	$str_length = strlen ( $sourcestr ); //字符串的字节数
	if( $cutlength == 0 ) $cutlength = $str_length - $cutstart;
	while ( ($n < $cutstart + $cutlength) and ($i <= $str_length) ) {
		$temp_str = substr ( $sourcestr, $i, 1 );
		$ascnum = Ord ( $temp_str ); //得到字符串中第$i位字符的ascii码
		if ($ascnum >= 224) //如果ASCII位高与224，
		{
			if($n >= $cutstart) $returnstr .= substr ( $sourcestr, $i, 3 ); //根据UTF-8编码规范，将3个连续的字符计为单个字符
			$i = $i + 3; //实际Byte计为3
			++ $n;
		}
		elseif ($ascnum >= 192) //如果ASCII位高与192，
		{
			if($n >= $cutstart) $returnstr .= substr ( $sourcestr, $i, 2 ); //根据UTF-8编码规范，将2个连续的字符计为单个字符
			$i = $i + 2; //实际Byte计为2
			++ $n;
		}
		elseif ($ascnum >= 65 && $ascnum <= 90) //如果是大写字母，
		{
			if($n >= $cutstart) $returnstr .= substr ( $sourcestr, $i, 1 );
			$i = $i + 1; //实际的Byte数仍计1个
			++ $n;
		}
		else //其他情况下，包括小写字母和半角标点符号，
		{
			if($n >= $cutstart) $returnstr .= substr ( $sourcestr, $i, 1 );
			$i = $i + 1; //实际的Byte数计1个
			++ $n;
		}
	}

	return $returnstr;
}