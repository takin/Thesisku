\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{ugmthesis}[2001/12/26 v1.0 The University of Gadjah Mada Thesis Class]

\RequirePackage[english,bahasa]{babel}
\RequirePackage{indentfirst}
\RequirePackage{setspace}
\RequirePackage[T1]{fontenc}
\RequirePackage{times}
\RequirePackage{graphicx,latexsym}

\newif\if@msthesis
\newif\if@msproposal
\DeclareOption{tesis}{\@msthesistrue\@msproposalfalse}
\DeclareOption{proposal}{\@msthesisfalse\@msproposaltrue}

\newif\if@singlesp
\newif\if@doublesp
\DeclareOption{satuspasi}{\@singlesptrue\@doublespfalse}
\DeclareOption{duaspasi}{\@singlespfalse\@doublesptrue}
\newif\if@onehalfsp
\@onehalfspfalse
\DeclareOption{satusetengahspasi}{\@onehalfsptrue}

\newif\if@langindo
\newif\if@langeng
\DeclareOption{indonesia}{\@langindotrue\@langengfalse}
\DeclareOption{inggris}{\@langindofalse\@langengtrue}

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{report}}
\ExecuteOptions{tesis,duaspasi,indonesia}
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

%------------------------------------------------------------
%Define page numbering in the first chapter
%------------------------------------------------------------
\def\ps@chapterheading{%
  \let\@evenhead\@empty\let\@oddhead\@empty
  \def\@oddfoot{{\slshape\rightmark}\hfil\thepage}%
  \def\@evenfoot{\thepage\hfil\slshape\leftmark}
  }

%------------------------------------------------------------
%Redefine chapter and sections
%------------------------------------------------------------
\setcounter{secnumdepth}{3}
\renewcommand \thepart {\@Roman\c@part}
\renewcommand \thechapter {\@Roman\c@chapter}
\renewcommand \thesection {\@arabic\c@section.}
\renewcommand\thesubsection   {\@alph\c@subsection.}
\renewcommand\thesubsubsection{\@roman\c@subsubsection.}
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
\renewcommand{\section}{\@startsection {section}{1}{\z@}%
                                   {-3.5ex \@plus -1ex \@minus -.2ex}%
                                   {2.3ex \@plus.2ex}%
                                   {\normalfont\normalsize\bfseries\centering}}
\renewcommand{\subsection}{\@startsection{subsection}{2}{\z@}%
                                     {-3.25ex\@plus -1ex \@minus -.2ex}%
                                     {1.5ex \@plus .2ex}%
                                     {\normalfont\normalsize\bfseries}}
\renewcommand{\subsubsection}{\@startsection{subsubsection}{3}{\parindent}%
                                    {3.25ex \@plus1ex \@minus.2ex}%
                                    {-1em}%
                                    {\normalfont\normalsize\bfseries}}
\renewcommand{\paragraph}{\subparagraph}

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

%English
\def\captionsenglish{%
\def\prefacename{PREFACE}%
\def\contentsname{CONTENTS}%
\def\listfigurename{LIST OF FIGURES}%
\def\listtablename{LIST OF TABLES}%
\def\listappendixname{LIST OF APPENDICES}%
\def\nomenclaturename{NOMENCLATURE}%
\def\abstractname{ABSTRACT}%
\def\partname{PART}%
\def\chaptername{CHAPTER}%
\def\appendixname{APPENDIX}%
\def\refname{REFERENCES}%
\def\bibname{REFERENCES}%
\def\indexname{Index}%
\def\figurename{Figure}%
\def\tablename{Table}%
\def\pagename{Page}%
}

%------------------------------------------------------------]
%Define thesis's inputs
%------------------------------------------------------------
\gdef\@universityind{Universitas Gadjah Mada}
\gdef\@programind{Program Pasca Sarjana}
\gdef\@universityeng{Gadjah Mada University}
\gdef\@programeng{Graduate Program}
\gdef\@city{Yogyakarta}
\newcommand{\headdept}[1]{\gdef\@headdept{#1}}
\newcommand{\@headdept}{}
\newcommand{\titleind}[1]{\gdef\@titleind{#1}}
\newcommand{\@titleind}{}
\newcommand{\titleeng}[1]{\gdef\@titleeng{#1}}
\newcommand{\@titleeng}{}
\newcommand{\fullname}[1]{\gdef\@fullname{#1}}
\newcommand{\@fullname}{}
\newcommand{\idnum}[1]{\gdef\@idnum{#1}}
\newcommand{\@idnum}{}
\newcommand{\deptind}[1]{\gdef\@deptind{#1}}
\newcommand{\@deptind}{}
\newcommand{\depteng}[1]{\gdef\@depteng{#1}}
\newcommand{\@depteng}{}
\newcommand{\examdate}[1]{\gdef\@examdate{#1}}
\newcommand{\@examdate}{\number\day~\ifcase\month\or
    Januari\or Pebruari\or Maret\or April\or Mei\or Juni\or
    Juli\or Agustus\or September\or Oktober\or Nopember\or Desember\fi
    \space \number\year}
\newcommand{\firstsupervisor}[1]{\gdef\@firstsupervisor{#1}}
\newcommand{\@firstsupervisor}{}
\newcommand{\secondsupervisor}[1]{\gdef\@secondsupervisor{#1}}
\newcommand{\@secondsupervisor}{}
\newcommand{\degreeind}[1]{\gdef\@degreeind{#1}}
\newcommand{\@degreeind}{Sarjana S-2}
\newcommand{\degreeeng}[1]{\gdef\@degreeeng{#1}}
\newcommand{\@degreeeng}{Master of Science}
\newcommand{\submitdate}[1]{\gdef\@submitdate{#1}}
\newcommand{\@submitdate}{}
\newcommand{\firstexaminer}[1]{\gdef\@firstexaminer{#1}}%
\newcommand{\@firstexaminer}{}
\newcommand{\secondexaminer}[1]{\gdef\@secondexaminer{#1}}%
\newcommand{\@secondexaminer}{}
\newcommand{\thirdexaminer}[1]{\gdef\@thirdexaminer{#1}}%
\newcommand{\@thirdexaminer}{}

%------------------------------------------------------------
%Define titlepage in Indonesian
%------------------------------------------------------------
\def\titlepageind{%
  \thispagestyle{empty}%
  \pagenumbering{roman}
  \setcounter{page}{1}
    \begin{center}
      {\normalfont\large\bfseries\@titleind}\par\nobreak
      \vspace{1.3cm}
      \begin{onehalfspace}
    \if@msthesis%
      {\normalfont Tesis\\
        untuk memenuhi sebagian persyaratan\\
        mencapai derajat \space\@degreeind}\\
     \else
      {\normalfont Usulan Penelitian untuk Tesis S-2}\\
      \fi
      \vspace{1.0cm}
      {\normalfont Program Studi \space\@deptind\\
        Kelompok Bidang Ilmu Matematika\\
        dan Pengetahuan Alam}\\
    \vfill
    \includegraphics{logougm}
    \vfill
      {\normalfont diajukan oleh\\
        \@fullname\\
        \@idnum}\\
    \vspace{1.0cm}
      {\normalfont Kepada\\
         \MakeUppercase{\@programind}\\
         \MakeUppercase{\@universityind}\\
        \MakeUppercase{\@city}\\
        \@submitdate}\\
      \end{onehalfspace}
      \end{center}
\addcontentsline{toc}{chapter}{Halaman Judul}
}  

%------------------------------------------------------------
%Define titlepage in English
%------------------------------------------------------------
\def\titlepageeng{%
  \newpage\thispagestyle{empty}%
    \begin{center}
      {\normalfont\large\bfseries\@titleeng}\par\nobreak
      \vspace{1.3cm}
      \begin{onehalfspace}
    \if@msthesis%
      {\normalfont Thesis\\
        as a partial fulfillment for the requirement of\\
        the degree of \space\@degreeeng}\\
     \else
      {\normalfont Research Proposal for Master Thesis}\\
      \fi
      \vspace{1.0cm}
      {\normalfont \expandafter{\@depteng} Study Program \\
        Department of Mathematics\\
        and Natural Sciences}\\
    \vfill
    \includegraphics{logougm}
    \vfill 
      {\normalfont submitted by\\
        \@fullname\\
        \@idnum}\\
    \vspace{1.0cm}
      {\normalfont To\\
         \MakeUppercase{\@programeng}\\
         \MakeUppercase{\@universityeng}\\
        \MakeUppercase{\@city}\\
        \@submitdate}\\
      \end{onehalfspace}
      \end{center}
}  

%------------------------------------------------------------
%Approval Page
%------------------------------------------------------------
\def\approvalpage{%
  \newpage\thispagestyle{empty}%
  \begin{center}
  \begin{onehalfspace}
    \if@msthesis%
  {\normalfont\large\bfseries\expandafter{TESIS}\\
    \@titleeng}\par\nobreak
  \vspace{1.0cm}
yang dipersiapkan dan disusun oleh\\
\@fullname\\
\@idnum\\
telah dipertahankan di depan Dewan Penguji\\
pada tanggal \expandafter{\@examdate}\\
\bigskip
\emph{Susunan Dewan Penguji}\\
\medskip
\begin{tabular}{p{8cm}p{6cm}}
Pembimbing Utama&Anggota Tim Penguji Lain
\end{tabular}

\vspace{1.3cm}
\begin{tabular}{p{8cm}p{6cm}}
\underline{\@firstsupervisor}&\underline{\@firstexaminer}
\end{tabular}

\bigskip
\begin{tabular}{p{8cm}p{6cm}}
Pembimbing Pendamping&
\end{tabular}

\vspace{1.3cm}
\begin{tabular}{p{8cm}p{6cm}}
\underline{\@secondsupervisor}&\underline{\@secondexaminer}
\end{tabular}

\bigskip
\vspace{1.3cm}
\begin{tabular}{p{8cm}p{6cm}}
&\underline{\@thirdexaminer}
\end{tabular}

\vfill
Tesis ini telah diterima sebagai salah satu persyaratan\\
untuk memperoleh gelar Magister

\medskip
Tanggal \space \@examdate

\vfill
\underline{\@headdept}\\
Pengelola Program Studi \space\@deptind
\else
  {\normalfont\large\bfseries\expandafter{Usulan Penelitian}\\
\vspace{0.6cm}
    \@titleeng}\par\nobreak
  \vspace{1.7cm}
yang diajukan oleh\\
\@fullname\\
\@idnum\\
\vfill
telah disetujui oleh :\\
\vspace{1cm}
\begin{tabular}{p{8cm}p{6cm}}
Pembimbing Utama&
\end{tabular}

\vspace{2.0cm}
\begin{tabular}{p{8cm}p{6cm}}
\underline{\@firstsupervisor}&tanggal\dotfill
\end{tabular}

\vspace{1cm}
\begin{tabular}{p{8cm}p{6cm}}
Pembimbing Pendamping&
\end{tabular}

\vspace{2.0cm}
\begin{tabular}{p{8cm}p{6cm}}
\underline{\@secondsupervisor}&tanggal\dotfill
\end{tabular}
\fi
    \end{onehalfspace}
    \end{center}
\addcontentsline{toc}{chapter}{Halaman Pengesahan}
}

%------------------------------------------------------------
%Declare Page
%------------------------------------------------------------
\def\declarepage{%
  \newpage\thispagestyle{chapterheading}%
  \begin{center}
  {\normalfont\large\bfseries\expandafter{PERNYATAAN}}
  \par\nobreak
  \end{center}
  \vspace{1.0cm}
    \noindent
Dengan ini saya menyatakan bahwa dalam tesis ini tidak terdapat karya yang
pernah diajukan untuk memperoleh gelar kesarjanaan di suatu Perguruan Tinggi, 
dan sepanjang pengetahuan saya juga tidak terdapat karya atau pendapat yang
ditulis atau diterbitkan oleh orang lain, kecuali yang secara tertulis diacu
dalam naskah ini dan disebutkan dalam daftar pustaka.
 
\vspace{1.5cm}
\begin{tabular}{p{7.5cm}c}
&\@city,\space\@examdate\\
&\\
&\\
&\space\@fullname
\end{tabular}
\vfill
\addcontentsline{toc}{chapter}{Halaman Pernyataan}
}

%------------------------------------------------------------
%Acknowlegment
%------------------------------------------------------------
\def\acknowledment{%
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
\def\preface{%
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
\def\abstractind{%
  \clearpage\thispagestyle{chapterheading}%
  {\parindent \z@ \centering \normalfont
   {\large\textbf{\@titleind}}
        \par\nobreak
        \vskip 6\p@
    \interlinepenalty\@M
    {\normalsize oleh\par \@fullname}\par\nobreak
    \vskip 10\p@
    }  
  \begin{singlespace}%
  \section*{\abstractname}%
  \vskip 20\p@
  \addcontentsline{toc}{chapter}{\abstractname}
}
\def\endabstractind{\par\vfil\null\end{singlespace}}

%------------------------------------------------------------
%Redefine Abstract in English
%------------------------------------------------------------
\def\abstracteng{%
  \clearpage\thispagestyle{chapterheading}\selectlanguage{english}%
  {\parindent \z@ \centering \normalfont
   {\large\textbf{\@titleeng}}
        \par\nobreak
        \vskip 6\p@
    \interlinepenalty\@M
    {\normalsize oleh\par \@fullname}\par\nobreak
    \vskip 10\p@
    }  
  \begin{singlespace}%
  \section*{\abstractname}%
  \vskip 20\p@
  \addcontentsline{toc}{chapter}{\abstractname}
  }
\def\endabstracteng{\par\vfil\null\end{singlespace}%
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

\AtBeginDocument{%
\if@langindo%
\selectlanguage{bahasa}%
\else
\selectlanguage{english}%
\fi
}

\endinput
