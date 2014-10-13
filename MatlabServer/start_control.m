function sFilePath = start_control()

sImages = fullfile(pwd, 'images');

sDateTemplate = java.lang.String('yyyyMMddHHmmss');
sFileName = sprintf('%s.jpg', ...
    char(java.text.SimpleDateFormat(sDateTemplate).format(java.util.Date())));

sFilePath = fullfile(sImages, sFileName);
receive_data(8888, sFilePath);
return;
end