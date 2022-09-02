from flask import Flask
import platform,socket,re,uuid,json,psutil,logging

app = Flask(__name__)

def getSystemInfo():
    try:
        info={}
        info['platform']=platform.system()
        info['platform-release']=platform.release()
        info['platform-version']=platform.version()
        info['architecture']=platform.machine()
        info['hostname']=socket.gethostname()
        info['ip-address']=socket.gethostbyname(socket.gethostname())
        info['mac-address']=':'.join(re.findall('..', '%012x' % uuid.getnode()))
        info['processor']=platform.processor()
        info['ram']=str(round(psutil.virtual_memory().total / (1024.0 **3)))+" GB"
        return json.dumps(info)
    except Exception as e:
        logging.exception(e)

@app.route('/sysinfo', methods=['GET'])
def get_system_info():
    return getSystemInfo()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port='5001', debug=True)