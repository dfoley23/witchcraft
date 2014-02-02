
folder_path = "./"
Dir.glob(folder_path + "/*").sort.each_with_index do |f,i|
    filename = File.basename(f, File.extname(f))
    print(filename+": ")
    filenum = filename.gsub(/[^0-9]/, '').to_i
    str = "%04d" % (filenum)
    print(str+"\n")
    File.rename(f, str + File.extname(f))
end
