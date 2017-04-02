import socket
import traceback
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind(('0.0.0.0', 1990))
while 1:
	try:
		message = s.recvfrom(1000)
		print(message)
		
	except (KeyboardInterrupt, SystemExit):
		 raise
	except:
		traceback.print_exc()
		pass