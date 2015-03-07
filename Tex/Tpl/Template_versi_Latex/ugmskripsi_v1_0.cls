\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{ugmskripsi}[2003/01/06 v1.0 The University of Gadjah Mada Skripsi Class]

\RequirePackage[bahasa]{babel}
\RequirePackage{indentfirst}
\RequirePackage{setspace}
\RequirePackage[T1]{fontenc}
\RequirePackage{times}
\RequirePackage{graphicx,latexsym}

\newif\if@sarskripsi
\newif\if@sarproposal
\DeclareOption{skripsi}{\@sarskripsitrue\@sarproposalfalse}
\DeclareOption{proposal}{\@sarskripsifalse\@sarproposaltrue}

\newif\if@singlesp
\newif\if@doublesp
\DeclareOption{satuspasi}{\@singlesptrue\@doublespfalse}
\DeclareOption{duaspasi}{\@singlespfalse\@doublesptrue}
\newif\if@onehalfsp
\@onehalfspfalse
\DeclareOption{satusetengahspasi}{\@onehalfsptrue}

%\newif\if@langindo
%\newif\if@langeng
%\DeclareOption{indonesia}{\@langindotrue\@langengfalse}
%\DeclareOption{inggris}{\@langindofalse\@langengtrue}

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{report}}
\ExecuteOptions{skripsi,duaspasi}
\ProcessOptions
\LoadClass[a4paper,12pt]{report}
\RequirePackage{natbib}
\bibliographystyle{plainnat}
\RequirePackage{nomencl}%
\renewcommand{\nomname}{\nomenclaturename}%
\makeglossary%

%------------------------------------------------------------
%Layout
%------------------------------------------------------------
\setlength{\topmargin}{-0.9cm}
\setlength{\headheight}{12pt}
\setlength{\headsep}{2.3cm}
\setlength{\topskip}{1ex}
\setlength{\oddsidemargin}{1.46cm}
\setlength{\evensidemargin}{1.46cm}
\setlength{\textwidth}{14.3cm}
\setlength{\textheight}{22cm}
\setlength{\footskip}{1.5cm}
\setlength{\marginparsep}{0pt}
\setlength{\marginparwidth}{0pt}

\setlength{\parindent}{3em} %Indent approx 6 character

%------------------------------------------------------------
%The form of page numbering
%------------------------------------------------------------
\pagestyle{myheadings}
\markright{}
%\pagestyle{plain}

%------------------------------------------------------------
%Define page numbering in the first chapter
%------------------------------------------------------------
\def\ps@chapterheading{%
  \let\@evenhead\@empty\let\@oddhead\@empty
  \def\@oddfoot{\hfil\thepage\hfil}%
  \def\@evenfoot{\hfil\thepage\hfil}
  }

%------------------------------------------------------------
%Redefine chapter and sections
%------------------------------------------------------------
\setcounter{secnumdepth}{3}
\renewcommand \thepart {\@Roman\c@part}
\renewcommand \thechapter {\@Roman\c@chapter}
\renewcommand \thesection {\@arabic\c@section.}
\renewcommand\thesubsection   {\@alph\c@subsection.}
\renewcommand\appendix{\par
  \setcounter{chapter}{0}%
  \setcounter{section}{0}%
  \gdef\@chapapp{\appendixname}%
  \gdef\thechapter{\@Alph\c@chapter}}
\renewcommand{\chapter}{\clearpage\thispagestyle{chapterheading}%
  \global\@topnum\z@ %Prevents figures from going at top of page
  \@afterindenttrue %Indent the 1st paragraph
  \secdef\@chapter\@schapter}
\renewcommand{\@makechapterhead}[1]{%
  {\parindent \z@ \centering \normalfont
    \ifnum \c@secnumdepth >\m@ne
        \large\bfseries \@chapapp\space \thechapter
        \par\nobreak
        \vskip 5\p@
    \fi
    \interlinepenalty\@M
    \large \bfseries #1\par\nobreak
    \vskip 20\p@
    }}
\renewcommand{\@makeschapterhead}[1]{%
  {\parindent \z@ \centering \normalfont
    \interlinepenalty\@M \large \bfseries #1\par\nobreak \vskip 20\p@ }}
\renewcommand{\section}{\@startsection{section}{1}{\parindent}%
                                     {-3.5ex\@plus -1ex \@minus -.2ex}%
                                     {2.3ex \@plus .2ex}%
                                     {\normalfont\normalsize\bfseries}}
\renewcommand{\subsection}{\@startsection{subsection}{2}{\z@}%
                                    {3.25ex \@plus1ex \@minus.2ex}%
                                    {-1em}%
                                    {\normalfont\normalsize\bfseries}}

%------------------------------------------------------------
%Redefine caption names
%------------------------------------------------------------
\def\captionsbahasa{%
\def\prefacename{PRAKATA}%
\def\contentsname{DAFTAR ISI}%
\def\listfigurename{DAFTAR GAMBAR}%
\def\listtablename{DAFTAR TABEL}%
\def\listappendixname{DAFTAR LAMPIRAN}%
\def\nomenclaturename{DAFTAR LAMBANG DAN SINGKATAN}%
\def\abstractname{INTISARI}%
\def\partname{BAGIAN}%
\def\chaptername{BAB}%
\def\appendixname{LAMPIRAN}%
\def\refname{DAFTAR PUSTAKA}%
\def\bibname{DAFTAR PUSTAKA}%
\def\indexname{Indek}%
\def\figurename{Gambar}%
\def\tablename{Tabel}%
\def\pagename{Halaman}%
}


%------------------------------------------------------------]
%Define thesis's inputs
%------------------------------------------------------------
\gdef\@universityind{Universitas Gadjah Mada}
\gdef\@programind{Sarjana S1}
\gdef\@cityind{Yogyakarta}
\gdef\@gelar{Sarjana S1}
\newcommand{\judul}[1]{\gdef\@judul{#1}}
\newcommand{\@judul}{}
\newcommand{\nama}[1]{\gdef\@nama{#1}}
\newcommand{\@nama}{}
\newcommand{\nim}[1]{\gdef\@nim{#1}}
\newcommand{\@nim}{}
\newcommand{\prodi}[1]{\gdef\@prodi{#1}}
\newcommand{\@prodi}{}
\newcommand{\jurusan}[1]{\gdef\@jurusan{#1}}
\newcommand{\@jurusan}{}
\newcommand{\thselesai}[1]{\gdef\@thselesai{#1}}
\newcommand{\@thselesai}{}
\newcommand{\tglujian}[1]{\gdef\@tglujian{#1}}
\newcommand{\@tglujian}{\number\day~\ifcase\month\or
    Januari\or Pebruari\or Maret\or April\or Mei\or Juni\or
    Juli\or Agustus\or September\or Oktober\or Nopember\or Desember\fi
    \space \number\year}
\newcommand{\pembimbingsatu}[1]{\gdef\@pembimbingsatu{#1}}
\newcommand{\@pembimbingsatu}{}
\newcommand{\pembimbingdua}[1]{\gdef\@pembimbingdua{#1}}
\newcommand{\@pembimbingdua}{}
\newcommand{\pengujisatu}[1]{\gdef\@pengujisatu{#1}}%
\newcommand{\@pengujisatu}{}
\newcommand{\pengujidua}[1]{\gdef\@pengujidua{#1}}%
\newcommand{\@pengujidua}{}
\newcommand{\pengujitiga}[1]{\gdef\@pengujitiga{#1}}%
\newcommand{\@pengujitiga}{}

%------------------------------------------------------------
%Define cover in Indonesian
%------------------------------------------------------------
\def\halamansampul{%
  \thispagestyle{empty}%
    \begin{center}
      {\normalfont\bfseries SKRIPSI}\par\nobreak
      \vspace{1.0cm}
      {\normalfont\large\bfseries\MakeUppercase{\@judul}}\par\nobreak
      \vspace{1.5cm}
      {\normalfont 
        \@nama\\
        \@nim}\\
    \vfill
    \includegraphics{logougm}
    \vfill
      \begin{onehalfspace}
      {\normalfont\bfseries 
         Departemen Pendidikan Nasional\\
         Universitas Gadjah Mada\\
         {\large Fakultas Matematika dan Ilmu Pengetahuan Alam}\\
         Yogyakarta\\
         \@thselesai}\\
      \end{onehalfspace}
      \end{center}
}  
%------------------------------------------------------------
%Define titlepage in Indonesian
%------------------------------------------------------------
\def\halamanjudul{%
  \newpage\thispagestyle{empty}%
  \pagenumbering{roman}
  \setcounter{page}{1}
    \begin{center}
      {\normalfont\bfseries SKRIPSI}\par\nobreak
      \vspace{1.0cm}
      {\normalfont\large\bfseries\MakeUppercase{\@judul}}\par\nobreak
      \vspace{1.3cm}
      {\normalfont 
        \@nama\\
        \@nim}\\
      \vspace{1.3cm}
      \begin{onehalfspace}
      {\normalfont 
        Sebagai salah satu syarat untuk memperoleh\\
        derajat \space\@gelar \space Program Studi \space\@prodi \space pada Jurusan \space\@jurusan}\\
    \vfill
    \includegraphics{logougm}
    \vfill
      {\normalfont\bfseries 
         Departemen Pendidikan Nasional\\
         Universitas Gadjah Mada\\
         {\large Fakultas Matematika dan Ilmu Pengetahuan Alam}\\
         Yogyakarta\\
         \@thselesai}\\
      \end{onehalfspace}
      \end{center}
\addcontentsline{toc}{chapter}{Halaman Judul}
}  

%------------------------------------------------------------
%Approval Page
%------------------------------------------------------------
\def\halamanpengesahan{%
  \newpage\thispagestyle{empty}%
  \begin{center}
  \begin{onehalfspace}
  {\normalfont\bfseries SKRIPSI}\par\nobreak
  \vspace{0.6cm}
  {\normalfont\large\bfseries\MakeUppercase{\@judul}}\par\nobreak
  \vspace{1.0cm}
\@nama\\
\@nim\\
\vspace{1.2cm}
Dinyatakan lulus ujian skripsi oleh tim penguji\\
pada tanggal \expandafter{\@tglujian}\\
\vspace{1.5cm}
{\bfseries{Tim Penguji}}\\
\vspace{2.7cm}
\begin{tabular}{p{8cm}p{6cm}}
\underline{\@pembimbingsatu}&\underline{\@pengujisatu}\\
Pembimbing I&Penguji I
\end{tabular}

\vspace{1.9cm}
\begin{tabular}{p{8cm}p{6cm}}
\underline{\@pembimbingdua}&\underline{\@pengujidua}\\
Pembimbing II&Penguji II
\end{tabular}

\vspace{1.9cm}
\begin{tabular}{p{8cm}p{6cm}}
&\underline{\@pengujitiga}\\
&Penguji III
\end{tabular}

    \end{onehalfspace}
    \end{center}
\addcontentsline{toc}{chapter}{Halaman Pengesahan}
}

%------------------------------------------------------------
%Acknowlegment
%------------------------------------------------------------
\def\persembahan{%
  \clearpage\thispagestyle{chapterheading}%
\null\vfill%Force the content of acknowledment to bottom page 
\addcontentsline{toc}{chapter}{Halaman Persembahan}
}

%------------------------------------------------------------
%Motto
%------------------------------------------------------------
\def\motto{%
  \clearpage\thispagestyle{chapterheading}%
\null\vfill%Force the content of acknowledment to bottom page 
\addcontentsline{toc}{chapter}{Halaman Motto}
}

%------------------------------------------------------------
%Preface
%------------------------------------------------------------
\def\pengantar{%
  \chapter*{\prefacename}%
\addcontentsline{toc}{chapter}{\prefacename}
}

%------------------------------------------------------------
%Nomenclature
%------------------------------------------------------------
\def\lambang{%
  \clearpage\thispagestyle{chapterheading}%
  \printglossary%
\addcontentsline{toc}{chapter}{\nomenclaturename}
}

%------------------------------------------------------------
%Redefine Abstract in Indonesian
%------------------------------------------------------------
\def\intisari{%
  \clearpage\thispagestyle{chapterheading}%
  {\parindent \z@ \centering \normalfont
   {\normalsize\bfseries \abstractname}\par\nobreak
   \vskip 10\p@
   {\large\bfseries\@judul}
        \par\nobreak
        \vskip 6\p@
    \interlinepenalty\@M
    {\normalsize Oleh :}\par\nobreak
    \vskip 6\p@
    {\normalsize \@nama\\ \@nim}\par\nobreak
    } 
  \vspace{1.5cm}
  \begin{singlespace}%
  \addcontentsline{toc}{chapter}{\abstractname}
}
\def\endintisari{\par\vfil\null\end{singlespace}
\selectlanguage{bahasa}\clearpage\pagenumbering{arabic}\setcounter{page}{1}}



\AtBeginDocument{%
\if@doublesp%
\doublespacing%
\else\if@singlesp%
\singlespacing%
\else
\onehalfspacing%
\fi%
\fi
}

\endinput
