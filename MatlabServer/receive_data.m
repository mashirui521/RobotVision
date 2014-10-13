function receive_data(nPort, filePath)

server = java.net.ServerSocket(nPort);
socket = server.accept();
try
    stream = java.io.DataInputStream(socket.getInputStream());
    file = java.io.File(java.lang.String(filePath));
    
    if file.exists()
        delete(filePath);
    end
    file.createNewFile();
    
    fileStream = java.io.FileOutputStream(file);
    
    length = stream.readInt();
    if length > 0
        for i = 1:length
            fileStream.write(stream.readByte());
        end
        fileStream.close();
    end
catch
    disp('error');
end

stream.close();
socket.close();
server.close();
end