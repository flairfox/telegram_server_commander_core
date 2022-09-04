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

def getResourceUtilizationInfo():
    try:
        info={}
        info['cpu-utilization']=psutil.cpu_percent(interval=None, percpu=True)
        info['memory-utilization']=psutil.virtual_memory().percent
        info['swap-utilization']=psutil.swap_memory().percent
        return json.dumps(info)
    except Exception as e:
        logging.exception(e)

def getDrivesInfo():
    try:
        info=[]
        disk_partitions=psutil.disk_partitions(all=True)
        for disk_partition in disk_partitions:
            disk_usage=psutil.disk_usage(disk_partition.mountpoint)

            drive_info={}
            drive_info['mount-point']=disk_partition.mountpoint
            drive_info['total']=disk_usage.total
            drive_info['used']=disk_usage.used
            drive_info['free']=disk_usage.free
            drive_info['percent']=disk_usage.percent

            info.append(drive_info)

        return json.dumps(info)
    except Exception as e:
        logging.exception(e)

@app.route('/sysinfo', methods=['GET'])
def get_system_info():
    return getSystemInfo()

@app.route('/utilization', methods=['GET'])
def get_resource_utilization_info():
    return getResourceUtilizationInfo()

@app.route('/drives', methods=['GET'])
def get_drives_info():
    return getDrivesInfo()


if __name__ == '__main__':
    app.run(host='0.0.0.0', port='5001', debug=True)