<?php

if( !defined( 'MW_PHP4' ) ) {
	die( "Not an entry point.");
}

if( isset( $_SERVER['SCRIPT_NAME'] ) ) {
	// Probably IIS; doesn't set REQUEST_URI
	$scriptUrl = $_SERVER['SCRIPT_NAME'];
} elseif( isset( $_SERVER['REQUEST_URI'] ) ) {
	// We're trying SCRIPT_NAME first because it won't include PATH_INFO... hopefully
	$scriptUrl = $_SERVER['REQUEST_URI'];
} else {
	$scriptUrl = '';
}
if ( preg_match( '!^(.*)/[^/]*.php$!', $scriptUrl, $m ) ) {
	$baseUrl = $m[1];
} else {
	$baseUrl = dirname( $scriptUrl );
}

?>
		<div class='error'>
<p>
			Infoecos requires PHP 5.0.0 or higher. You are running PHP
			<?php echo htmlspecialchars( phpversion() ); ?>.
</p>
<?php
flush();
?>
<p>Please consider
<a href="http://www.php.net/downloads.php">upgrading your copy of PHP</a>.
PHP 4 is at the end of its lifecycle and will not receive further security updates.</p>

		</div>
