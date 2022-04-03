<?php

if ( ini_get( 'register_globals' ) ) {
	if ( isset( $_REQUEST['GLOBALS'] ) ) {
		die( '<a href="http://www.hardened-php.net/index.76.html">$GLOBALS overwrite vulnerability</a>');
	}
	$verboten = array(
		'GLOBALS',
		'_SERVER',
		'HTTP_SERVER_VARS',
		'_GET',
		'HTTP_GET_VARS',
		'_POST',
		'HTTP_POST_VARS',
		'_COOKIE',
		'HTTP_COOKIE_VARS',
		'_FILES',
		'HTTP_POST_FILES',
		'_ENV',
		'HTTP_ENV_VARS',
		'_REQUEST',
		'_SESSION',
		'HTTP_SESSION_VARS'
	);
	foreach ( $_REQUEST as $name => $value ) {
		if( in_array( $name, $verboten ) ) {
			header( "HTTP/1.x 500 Internal Server Error" );
			echo "register_globals security paranoia: trying to overwrite superglobals, aborting.";
			die( -1 );
		}
		unset( $GLOBALS[$name] );
	}
}

$wgRequestTime = microtime(true);
# getrusage() does not exist on the Microsoft Windows platforms, catching this
if ( function_exists ( 'getrusage' ) ) {
	$wgRUstart = getrusage();
} else {
	$wgRUstart = array();
}
unset( $IP );

$IP = realpath( '.' ) . '/../libs';



# Check for PHP 5
if ( !function_exists( 'version_compare' ) 
	|| version_compare( phpversion(), '5.0.0' ) < 0
) {
	define( 'MW_PHP4', '1' );
	require( "$IP/includes/PHP4.php" );
	exit;
}

# Test for PHP bug which breaks PHP 5.0.x on 64-bit...
# As of 1.8 this breaks lots of common operations instead
# of just some rare ones like export.
$borked = str_replace( 'a', 'b', array( -1 => -1 ) );
if( !isset( $borked[-1] ) ) {
	echo "PHP 5.0.x is buggy on your 64-bit system; you must upgrade to PHP 5.1.x\n" .
	     "or higher. ABORTING. (http://bugs.php.net/bug.php?id=34879 for details)\n";
	exit;
}

