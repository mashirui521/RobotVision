function rgbData = mls_supporter_receive(hService)

hService.receive();
hService.adaptByteToRGB();

dataR = hService.getR();
dataG = hService.getG();
dataB = hService.getB();

R = zeros(480, 640);
G = zeros(480, 640);
B = zeros(480, 640);
for i = 1:480
    R(i, :) = dataR((640 * (i - 1) + 1):(640*i));
    G(i, :) = dataG((640 * (i - 1) + 1):(640*i));
    B(i, :) = dataB((640 * (i - 1) + 1):(640*i));
end

rgbData(:, :, 1) = R./255;
rgbData(:, :, 2) = G./255;
rgbData(:, :, 3) = B./255;
return;
end