
folder_path = "./"
Dir.glob(folder_path + "/*").sort.each do |f|
    filename = File.basename(f, File.extname(f))
    print(filename+": ")
    filenum = filename.gsub(/[^0-9]/, '').to_i
    str = "level%04d" % filenum
    print(str+"\n")
    File.rename(f, str + File.extname(f))
end
